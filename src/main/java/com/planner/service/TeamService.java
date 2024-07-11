package com.planner.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.planner.dto.request.team.MyTeamListDTO;
import com.planner.dto.request.team.TeamDTO;
import com.planner.dto.request.team.TeamInfoDTO;
import com.planner.dto.request.team.TeamMemberDTO;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.TM_Grade;
import com.planner.mapper.TeamMapper;
import com.planner.mapper.TeamMemberMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TeamService {

	private final TeamMapper teamMapper;
	private final TeamMemberMapper tmMapper;
	// upload 폴더 경로
	private final String uploadPath = new File("").getAbsolutePath() +"\\src\\main\\resources\\static\\upload\\";
	// 지원하는 이미지 확장자 리스트
	private final List<String> exts = Arrays.asList(ImageIO.getReaderFormatNames()); 

	/** 그룹 이름 중복 검사. 중복이면 true */
	public boolean teamNameOverlap(String team_name) {
		return teamMapper.teamNameOverlap(team_name) == 1 ? true : false;
	}

	// 그룹 이미지 리사이즈 후 저장
	public boolean setImg(TeamDTO dto, MultipartFile team_image) {
		String team_name = dto.getTeam_name();
		if(!team_image.isEmpty()) { // 업로드한 파일이 있고
			if(team_image.getContentType().startsWith("image")) { // 그게 이미지라면
				String imgName = team_image.getOriginalFilename();
				String ext = imgName.substring(imgName.lastIndexOf(".")+1); // 확장자
				String ran_uuid = UUID.randomUUID().toString();
				String pathName = uploadPath + team_name + ran_uuid + "." + ext; // 경로 + 이름 + 중복방지 + 확장자
				if(!exts.contains(ext)) { // ImageIO가 지원하는 확장자가 아니라면
					return false;
				}
				// 이미지 리사이즈
				try {
					BufferedImage bufImg = ImageIO.read(team_image.getInputStream());
					// gif는 움짤인 경우 bufImg == null
					if(bufImg == null) {
						return false;
					}
					// ARGB로 다 처리하면 투명도 지원 안되는 jpg같은 종류는 이미지 저장이 안됨
					BufferedImage reImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
                    Graphics2D graphics = reImg.createGraphics();
                    if (bufImg.getWidth() > 100 || bufImg.getHeight() > 100) { // 이미지 가로 세로가 100보다 크면
                        Image resize = bufImg.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        graphics.drawImage(resize, 0, 0, null);
                        ImageIO.write(reImg, ext, new File(pathName));
                    } else { // 이미지 가로 세로 둘다 100 이하면
                        graphics.drawImage(bufImg, 0, 0, null);
                        ImageIO.write(bufImg, ext, new File(pathName));
                    }
                    dto.setTeam_image(team_name+ran_uuid+"."+ext);
                    graphics.dispose();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	// 그룹 이미지 제거
	public void delImg(TeamDTO dto) {
		File img = new File(uploadPath+dto.getTeam_image());
		img.delete();
	}

	// 그룹 생성
	@Transactional
	public long teamCreate(ResMemberDetail detail, String team_name, String team_explain, MultipartFile team_image) {
		TeamDTO dto = TeamDTO.builder()
						.team_name(team_name)
						.team_explain(team_explain)
						.build();
		boolean imgResult = this.setImg(dto, team_image);
		if(!imgResult) { // team_image가 검사를 통과 못했으면
			return -1;
		}

		teamMapper.teamInsert(dto); // team 생성

		TeamMemberDTO tmdto = TeamMemberDTO.builder()
								.member_id(detail.getMember_id())
								.team_id(dto.getTeam_id())
								.tm_nickname(detail.getMember_name())
								.tm_grade(TM_Grade.ROLE_TEAM_MASTER.getValue())
								.build();
		// team을 생성한 member를 해당 team의 team_member로 추가
		try {
			// 그룹 내에서 닉네임 중복일 때 예외 발생하는데, 그룹 생성시에는 중복일 수 없음.
			tmMapper.insertTeamMember(tmdto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto.getTeam_id();
	}

	// 그룹 정보 읽기
	public TeamDTO teamInfo(long team_id) {
		return teamMapper.teamInfo(team_id);
	}
	
	// 그룹 정보 + 그룹장 email 읽기
	public TeamInfoDTO teamAndMasterInfo(long team_id) {
		return teamMapper.teamAndMasterInfo(team_id);
	}

	// 이미지 파일 읽기
	public ResponseEntity<Resource> teamImg(String imgName) {
		String pathName = uploadPath + imgName;
		Resource re = new FileSystemResource(pathName);
		if(!re.exists()) { // 없으면 404
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		}
		HttpHeaders head = new HttpHeaders();
		Path filePath = null;
		filePath = Paths.get(pathName);
		try {
			head.add("content-type", Files.probeContentType(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Resource>(re, head, HttpStatus.OK);
	}

	// 그룹 정보 수정
	public void teamInfoUpdate(long team_id, String team_name, String team_explain,
								MultipartFile team_image, String delimg) {
		TeamDTO dto = teamMapper.teamInfo(team_id);
		dto = TeamDTO.builder()
				.team_id(team_id)
				.team_name(team_name)
				.team_explain(team_explain)
				.team_image(dto.getTeam_image())
				.build();
		boolean del = delimg.equals("delimg"); // 기존 이미지 제거 체크했으면 true
		boolean newImg = !team_image.isEmpty(); // 교체할 이미지 있으면 true
		if(del || newImg) { // 기존 이미지를 제거, 혹은 교체시
			this.delImg(dto);
			if(newImg) {
				this.setImg(dto, team_image);
			}else if(del) {
				dto.setTeam_image(null);
			}
		}
		teamMapper.teamUpdate(dto);
	}

	// 그룹 제거
	public void teamDelete(long member_id, long team_id) {
		TeamDTO dto = teamMapper.teamInfo(team_id);
		int result = teamMapper.teamDelete(member_id, team_id);
		if(result == 1) {
			if(dto.getTeam_image() != null) {
				this.delImg(dto);
			}
		}
	}

	// 가입 그룹 목록
	public List<MyTeamListDTO> myTeamList(Long member_id) {
		return teamMapper.myTeamList(member_id);
	}

	// 그룹 검색
	public List<TeamInfoDTO> teamListSearch(int pageSize, long page, String searchOption, String search) {
		long start = (page-1)*pageSize + 1;
		long end = page*pageSize;
		return teamMapper.teamListSearch(start, end, searchOption, search);
	}

	// 검색된 그룹 수 count
	public long teamCount(String searchOption, String search) {
		return teamMapper.teamCount(searchOption, search);
	}
}

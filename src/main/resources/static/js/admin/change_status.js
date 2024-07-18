  function updateMemberStatus() {
            const member_status = document.getElementById("member_status").value;
            console.log(member_status);
            location.href = '/admin/memberAllStatus?member_status=' + member_status;
        }


        function update(btn){
        	const member_id = btn.value;
            const tr = btn.closest('tr');
            const member_status = tr.querySelector('.member_update_status').value;
        	console.log(member_status);
        	location.href = '/admin/memberStatusUpdate?member_id=' + member_id + '&member_status=' + member_status;
        } 
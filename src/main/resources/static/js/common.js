// null 체크공동 메서드
function isNull(chkData){
	if(chkData == null || chkData == ""){
		return true;
	}
	return false;
}
// AJAX 공통 
    function ajaxCall(url,method,param,successFn,errorFn){
        $.ajax({
            url: url,
            method: method,
            data: param,
            success: function (data) {
               if(typeof successFn =="function"){
                   successFn(data);
               }
            },
            error: function () {
                if(typeof errorFn =="function"){
                    errorFn();
                }
            }
        });
    };
  $(function(){ 
        document.querySelectorAll(".updateStatus").forEach(function(button) {
            button.addEventListener("click", function() {
                let option = this.nextElementSibling;
                let confirmButton = this.parentElement.nextElementSibling;
                if (option.style.display === "none" || option.style.display === "") {
                    option.style.display = "block";
                    confirmButton.style.display = "block";
                } else {
                    option.style.display = "none";
                    confirmButton.style.display = "none";
                }
            });
        });
});
  function updateMemberStatus() {
            const member_status = document.getElementById("member_status").value;
            location.href = '/admin/memberAllStatus?member_status=' + member_status;
        }


        function update(btn){
        	const member_id = btn.value;
            const tr = btn.closest('tr');
            const member_status = tr.querySelector('.member_update_status').value;
        	console.log(member_status);
        	location.href = '/admin/memberStatusUpdate?member_id=' + member_id + '&member_status=' + member_status;
        } 
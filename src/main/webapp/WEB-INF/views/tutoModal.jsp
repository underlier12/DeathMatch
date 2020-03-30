<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tutorial</title>
<style type="text/css">
.modal-dialog{
	max-width: 100%; 
	width: auto; 
	display: table;
}
</style>
</head>
<body>
	<!-- Tutorial Modal -->
	<div class="modal fade" id="tutoModal" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h2 id="modal-title" class="modal-title">Tutorial</h2>
				</div>
				<div class="modal-body">
					<video id="clip" width="960" height="560" controls>
					    <source src="/genious/resources/videos/unionTutorial.mp4" type="video/mp4">
					</video>
				</div>
				<div class="modal-footer">
					<button onclick="stopClip()" class="btn btn-default btn-lg" data-dismiss="modal">닫기</button>
				</div>
			</div>
		</div>
	</div>
<script type="text/javascript">
	function stopClip(){
		document.getElementById("clip").pause();
	}
</script>
</body>
</html>
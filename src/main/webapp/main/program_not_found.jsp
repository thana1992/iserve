<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Caution</title>		
		<link href="../img/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<link rel="stylesheet" type="text/css" href="../css/base_style.css" />
		<link rel="stylesheet" type="text/css" href="../css/user_style.css" />
		<link rel="stylesheet" type="text/css" href="../css/program_style.css" />
		<style nonce="YXNzdXJl">
			a.back-link { font-size: 1.5rem; text-decoration: none; } 
			.portal-body { margin:0px; font-size:1rem; font-weight:400;line-height:1.5; }
			.pt-page-header { margin-top:0px; margin-bottom:0.5rem; }
		</style>
		<script nonce="YXNzdXJl">
			function doGoBack() {
				window.history.back();
			}
			window.onload = function() {
				gobacklinker.onclick = doGoBack;
			}
		</script>
	</head>
	<body class="portal-body portalbody-off">
		<div id="ptsearchpager" class="pt-page pt-page-current pt-page-controller">
			<div class="header-layer">
				<h1 class="pt-page-header page-header-title">
					<label>Caution</label>
				</h1>
			</div>
			<div id="searchpanel" class="panel-body">
				<div id="messagepanellayer" class="message-panel-class">
					Sorry, Page not found or under construction
				</div>
				<br/>
				<div id="gobacklayer" class="goback-class">
					<a href="#" id="gobacklinker" class="back-link">Go Back</a>
				</div>
			</div>
		</div>
	</body>
</html>

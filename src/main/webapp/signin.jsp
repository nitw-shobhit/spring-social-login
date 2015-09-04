<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" />
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" />
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-social/4.10.0/bootstrap-social.min.css" />
	</head>
	<body>
		<div style="margin-left:auto; margin-right:auto;">
			<h3>Sign in with :</h3><br><br>
			<table>
				<tr>
					<td style="padding-left: 20px;">
						<form action="/social-login/signin/facebook" method="POST">
						    <button type="submit" class="btn btn-social-icon btn-facebook"><i class="fa fa-facebook"></i></button>
						</form>
					</td>
					<td style="padding-left: 20px;">
						<form action="/social-login/signin/twitter" method="POST">
						    <button type="submit" class="btn btn-social-icon btn-twitter"><i class="fa fa-twitter"></i></button>
						</form>
					</td>
					<td style="padding-left: 20px;">
						<form action="/social-login/signin/google" method="POST">
						    <button type="submit" class="btn btn-social-icon btn-google"><i class="fa fa-google"></i></button>
						    <input type="hidden" name="scope" value="email" />
						</form>
					</td>
					<td style="padding-left: 20px;">
						<form action="/social-login/signin/linkedin" method="POST">
						    <button type="submit" class="btn btn-social-icon btn-linkedin"><i class="fa fa-linkedin"></i></button>
						</form>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>

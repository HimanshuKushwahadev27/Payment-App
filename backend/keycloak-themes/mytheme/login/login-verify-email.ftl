<!DOCTYPE html>
<html>
<head>

<title>Email Verification</title>

<link rel="stylesheet" href="${url.resourcesPath}/css/styles.css">

</head>

<body>

<div class="verify-container">

    <div class="verify-card">

        <h2>Email Verification</h2>

        <p>
            We've sent a verification email to
            <strong>${user.email}</strong>
        </p>

        <p>Please check your inbox and click the verification link.</p>

        <form action="${url.loginAction}" method="post">
            <button class="primary-btn">
                Resend Email
            </button>
        </form>

    </div>

</div>

</body>
</html>
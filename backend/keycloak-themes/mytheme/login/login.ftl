<#import "template.ftl" as layout>

<@layout.registrationLayout displayInfo=true; section>

<#if section == "header">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</#if>


<#if section == "form">

<div class="inkly-login">

    <h1 class="login-title">Just-Pay</h1>

    <#-- Error message -->
    <#if message?has_content>
        <div class="login-error">
            ${kcSanitize(message.summary)?no_esc}
        </div>
    </#if>
    
    


    <form id="kc-form-login"
          action="${url.loginAction}"
          method="post">

        <div class="form-group">
            <input
                type="text"
                id="username"
                name="username"
                placeholder="Email or Username"
                value="${(login.username!'')}"
                autofocus
                autocomplete="username"/>
        </div>

        <div class="form-group password-field">

            <input
                type="password"
                id="password"
                name="password"
                placeholder="Password"
                autocomplete="current-password"/>

            <span class="material-icons toggle-password"
                        onclick="togglePassword()">
                        visibility
            </span>

        </div>

        <button type="submit" class="login-btn">
            Login
        </button>

    </form>

</div>

<script>
function togglePassword(){
    const input = document.getElementById("password");
    input.type = input.type === "password" ? "text" : "password";
}
</script>

</#if>
<#if section == "socialProviders">
<div class="social-login">

    <#if social?? && social.providers?has_content>
        <#list social.providers as provider>
            <a class="social-btn ${provider.alias}"
               href="${provider.loginUrl}">
                Continue with ${provider.displayName}
            </a>
        </#list>
    </#if>

</div>
</#if>
</@layout.registrationLayout>
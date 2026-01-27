<#macro registrationLayout bodyClass="" displayInfo=false displayMessage=true displayRequiredFields=false>
<!DOCTYPE html>
<html lang="${(locale.currentLanguageTag)!'de'}">
<head>
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="robots" content="noindex, nofollow">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <#if properties.meta?has_content>
        <#list properties.meta?split(' ') as meta>
            <meta name="${meta?split('=')[0]}" content="${meta?split('=')[1]}"/>
        </#list>
    </#if>

    <title>
        <#if pageTitle?has_content>
            ${pageTitle} - ${realm.displayName!'EDAG'}
        <#else>
            ${msg("loginTitle",(realm.displayName!'EDAG'))}
        </#if>
    </title>
    <link rel="icon" href="${url.resourcesPath}/img/favicon.ico" />

    <#if properties.stylesCommon?has_content>
        <#list properties.stylesCommon?split(' ') as style>
            <link href="${url.resourcesCommonPath}/${style}" rel="stylesheet" />
        </#list>
    </#if>
    <#if properties.styles?has_content>
        <#list properties.styles?split(' ') as style>
            <link href="${url.resourcesPath}/${style}" rel="stylesheet" />
        </#list>
    </#if>
    <#if properties.scripts?has_content>
        <#list properties.scripts?split(' ') as script>
            <script src="${url.resourcesPath}/${script}" type="text/javascript"></script>
        </#list>
    </#if>
    <#if scripts??>
        <#list scripts as script>
            <script src="${script}" type="text/javascript"></script>
        </#list>
    </#if>
</head>

<body class="login-pf">
    <div class="login-pf">
        <div class="login-pf-page">
            <div class="card-pf">
                <!-- Logo -->
                <div class="kc-logo">
                    <img src="${url.resourcesPath}/img/logo.png"
                         alt="${realm.displayName!'EDAG'}" />
                </div>

                <!-- Header -->
                <header class="kc-header">
                    <h1>
                        <#nested "header">
                    </h1>
                </header>

                <!-- Messages -->
                <#if displayMessage && message?has_content && (message.type != 'warning' || !isAppInitiatedAction??)>
                    <div class="kc-${message.type}">
                        ${kcSanitize(message.summary)?no_esc}
                    </div>
                </#if>

                <!-- Main Form -->
                <div class="kc-form">
                    <#nested "form">
                </div>

                <!-- Social Providers -->
                <#if social?? && social.providers??>
                    <div class="kc-social-providers">
                        <div class="kc-social-divider">
                            <span>${msg("template.socialDivider")}</span>
                        </div>
                        <div class="kc-social-buttons">
                            <#list social.providers as p>
                                <a href="${p.loginUrl}" class="kc-social-button" data-provider="${p.providerId}">
                                    <span>${p.displayName}</span>
                                </a>
                            </#list>
                        </div>
                    </div>
                </#if>

                <!-- Footer -->
                <footer class="kc-footer">
                    <p>${msg("template.footer")}</p>
                </footer>
            </div>
        </div>
    </div>
</body>
</html>
</#macro>

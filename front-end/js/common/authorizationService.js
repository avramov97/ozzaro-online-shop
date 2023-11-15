var app = app || {};

let auth = {};

function saveCredentials(credentials)
{
    auth['token'] = credentials;
    auth['role'] = JSON.parse(
        atob(credentials.split('.')[1])
    )['role'];
    auth['sub'] = JSON.parse(
        atob(credentials.split('.')[1])
    )['sub'];
}

function getCredentials()
{
    if(auth['token']) return 'Bearer ' + auth['token'];
}

function getRole()
{
    if(auth['role']) return auth['role'];
}

function getUsername()
{
    if(auth['sub']) return auth['sub'];
}

function getUser()
{
    if(auth)
        return auth;
}

function evictCredentials()
{
    auth = {};
}

app.authorizationService = (function ()
{
    return {
        saveCredentials: saveCredentials,
        getCredentials: getCredentials,
        getRole: getRole,
        getUsername: getUsername,
        evictCredentials: evictCredentials,
        getUser: getUser,
    };
})();
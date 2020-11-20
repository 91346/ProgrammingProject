"use strict";
function UsersLogin() {
    //debugger;
    console.log("Invoked UsersLogin() ");
    let url = "/user/login";
    let formData = new FormData(document.getElementById('LoginForm'));
    fetch(url, {
        method: "POST",
        body: formData,
    }).then(response => {
        return response.json();                 //now return that promise to JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));        // if it does, convert JSON object to string and alert
        } else {
            Cookies.set("Token", response.Token);
            Cookies.set("Username", response.Username);
            window.open("index.html", "_self");       //open index.html in same tab
        }
    });
}

function logout() {
    debugger;
    console.log("Invoked logout");
    let url = "/user/logout";
    fetch(url, {method: "POST"
    }).then(response => {
        return response.json();                 //now return that promise to JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));        // if it does, convert JSON object to string and alert
        } else {
            Cookies.remove("Token", response.Token);    //Username and Token are removed
            Cookies.remove("Username", response.Username);
            window.open("index.html", "_self");       //open index.html in same tab
        }
    });
}

//addUser function to add a user to the database
function addUser() {
    console.log("Invoked AddUser()");
    const formData = new FormData(document.getElementById('InputUserDetails'));
    let url = "/user/add";
    fetch(url, {
        method: "POST",
        body: formData,
    }).then(response => {
        return response.json()
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));
        } else {
            window.open("/client/welcome.html", "_self");   //URL replaces the current page.  Create a new html file
        }                                                              //in the client folder called welcome.html
    });
}

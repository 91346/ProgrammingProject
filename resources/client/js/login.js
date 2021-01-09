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
        return response.json(); //now return that promise to JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));  // if it does, convert JSON object to string and alert
        } else {
            Cookies.set("Token", response.Token);
            Cookies.set("Username", response.Username);
            Cookies.set("Admin", response.Admin);
            window.open("dashboard.html", "_self"); //open home.html in same tab
        }
    });
}

function logout() {
    debugger;
    console.log("Invoked logout");
    let url = "/user/logout";
    fetch(url, {
        method: "POST",
    }).then(response => {
        return response.json();                 //now return that promise to JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));        // if it does, convert JSON object to string and alert
        } else {
            Cookies.remove("Token", response.Token);    //Username and Token are removed
            Cookies.remove("Username", response.Username);
            Cookies.remove("Admin", response.Admin);
            window.open("home.html", "_self");       //open home.html in same tab
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
            window.open("/user/dashboard.html", "_self");   //URL replaces the current page.  Create a new html file
        }                                                              //in the client folder called welcome.html
    });
}


//getUser() returns one row of data from the database using a GET and path parameter
function getUser() {
    console.log("Invoked getUser()");     //console.log your BFF for debugging client side
    const UserID = document.getElementById("UserID").value;  //get the UserId from the HTML element with id=UserID
    //let UserID = 1;               //You could hard code it if you have problems
    //debugger;                  //debugger statement to allow you to step through the code in console dev F12
    const url = "/users/getUser/";       // API method on webserver
    fetch(url + UserID, {                // UserID as a path parameter
        method: "GET",
    }).then(response => {
        return response.json();                         //return response to JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) {         //checks if response from server has an "Error"
            alert(JSON.stringify(response));            // if it does, convert JSON object to string and alert
        } else {
            document.getElementById("DisplayOneUser").innerHTML = response.UserID + " " + response.Username;  //output data
        }
    });
}


//let variable = Cookies.get('Admin'); //to get whether admin

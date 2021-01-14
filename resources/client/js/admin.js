"use strict";
function getUsersList() {
    debugger;
    console.log("Invoked getUsersList()");     //console.log your BFF for debugging client side - also use debugger statement
    const url = "/user/list/";    		// API method on web server will be in Users class, method list
    fetch(url, {
        method: "GET",				//Get method
    }).then(response => {
        return response.json();                 //return response as JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) { //checks if response from the web server has an "Error"
            alert(JSON.stringify(response));    // if it does, convert JSON object to string and alert (pop up window)
        } else {
            formatUsersList(response);          //this function will create an HTML table of the data (as per previous lesson)
        }
    });
}

function formatUsersList(myJSONArray){
    let dataHTML = "";
    for (let item of myJSONArray) {
        dataHTML += "<tr><td>" + item.UserID + "<td><td>" + item.Username + "<tr><td>";
    }
    document.getElementById("Users").innerHTML = dataHTML;
}

//getUser() returns one row of data from the database using a GET and path parameter
function getUser() {
    console.log("Invoked getUser()");  //console.log your BFF for debugging client side
    const UserID = document.getElementById("UserIDget").value;  //get the UserId from the HTML element with id=UserID
    debugger;	//debugger statement to allow you to step through the code in console dev F12
    const url = "/user/getUser/";  // API method on webserver
    fetch(url + UserID, {  // UserID as a path parameter
        method: "GET",
    }).then(response => {
        return response.json(); //return response to JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) { //checks if response from server has an "Error"
            alert(JSON.stringify(response)); // if it does, convert JSON object to string and alert
        } else {
            document.getElementById("DisplayOneUser").innerHTML = response.UserID + "<tr><td>" + response.Username;  //output data
        }
    });
}

//UNFINISHED
//getUser() returns one row of data from the database using a GET and path parameter
function updateUser() {
    console.log("Invoked updateUser()");  //console.log your BFF for debugging client side
    const UserID = document.getElementById("UserIDup").value;  //get the UserId from the HTML element with id=UserID
    debugger;	//debugger statement to allow you to step through the code in console dev F12
    const url = "/user/update/";  // API method on webserver
    fetch(url + UserID, {  // UserID as a path parameter
        method: "POST",
    }).then(response => {
        return response.json(); //return response to JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) { //checks if response from server has an "Error"
            alert(JSON.stringify(response)); // if it does, convert JSON object to string and alert
        } else {
            document.getElementById("DisplayUpdate").innerHTML = response;  //output data
        }
    });
}

//UNFINISHED
//getUser() returns one row of data from the database using a GET and path parameter
function deleteUser() {
    console.log("Invoked deleteUser()");  //console.log your BFF for debugging client side
    const UserID = document.getElementById("UserIDdel").value;  //get the UserId from the HTML element with id=UserID
    debugger;	//debugger statement to allow you to step through the code in console dev F12
    const url = "/user/delete/";  // API method on webserver
    fetch(url + UserID, {  // UserID as a path parameter
        method: "POST",
    }).then(response => {
        return response.json(); //return response to JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) { //checks if response from server has an "Error"
            alert(JSON.stringify(response)); // if it does, convert JSON object to string and alert
        } else {
            document.getElementById("DisplayDelete").innerHTML = response;  //output data
        }
    });
}
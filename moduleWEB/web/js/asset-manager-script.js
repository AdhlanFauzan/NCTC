$(document).ready(function () {
    setAssetManagerCars();
    setAssetManagerDrivers();
});

function setAssetManagerCars() {
    $.ajax({
        method: 'POST',
        url: 'api/admin_asset_manager/cars',
        dataType: 'text',
        success: function (data, textStatus, jqXHR) {
            var obj = JSON.parse(data);
            alert(data);
            drawTableCars(obj.cars, "cars");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(jqXHR.responseText + " Error!");
        }
    })
}
function drawTableCars(data, table) {
    for (var i = 0; i < data.length; i++) {
        drawRowCars(data[i], table);
    }
}
function drawRowCars(rowData, table) {
    myCarId = rowData.carId;
    var div = document.getElementById(table);
    var createDiv = document.createElement("div");
    var node = document.createTextNode("id :" + rowData.carId + " model :" + rowData.model);
    createDiv.appendChild(node);
    createDiv.className = "button";
    createDiv.onclick = function () {
        alert(rowData.carId)
        editCarById(rowData.carId);
    };
    div.appendChild(createDiv);
}
function editCarById(carId){
    alert(carId);
    $.ajax({
        method: 'POST',
        url: 'api/car/getCarDataById',
        contentType: "text/plain; charset=utf-8",
        data: carId,
        dataType: 'text',
        success: function (data, textStatus, jqXHR) {
            var obj = JSON.parse(data);
            alert(data);
            drawFormCar(obj.carData[0]);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(jqXHR.responseText + " Error!");
        }
    })
}
function drawFormCar(rowData){
    document.getElementById("idForCar").value = rowData.carId;
    document.getElementById("model").value = rowData.model;
    document.getElementById("seatCount").value = rowData.countSeat;
    document.getElementById("licencePlate").value = rowData.licencePlate;
    document.getElementById("classCar").value = rowData.classCar;
    document.getElementById("driverId").value = rowData.driverId;
    document.getElementById("requiredDriverCategory").value = rowData.requiredDriverCategory;
}

function setAssetManagerDrivers() {

    $.ajax({
        method: 'POST',
        url: 'api/admin_asset_manager/drivers',
        dataType: 'text',
        success: function (data, textStatus, jqXHR) {
            var obj = JSON.parse(data);
            alert(data);
            drawTableDrivers(obj.cars, "drivers");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(jqXHR.responseText + " Error!");
        }
    })
}
function drawTableDrivers(data, table) {
    for (var i = 0; i < data.length; i++) {
        drawRowDrivers(data[i], table);
    }
}
function drawRowDrivers(rowData, table) {
    var div = document.getElementById(table);
    var createDiv = document.createElement("div");
    var node = document.createTextNode("id :" + rowData.driverId + " name :" + rowData.name);
    createDiv.appendChild(node);
    createDiv.className = "button";
    createDiv.onclick = function () {
        editDriverById(rowData.driverId);
    };
    div.appendChild(createDiv);
}
function editDriverById(driverId){
    alert(driverId);
    $.ajax({
        method: 'POST',
        url: 'api/user/getUserDataById',
        contentType: "text/plain; charset=utf-8",
        data: driverId,
        dataType: 'text',
        success: function (data, textStatus, jqXHR) {
            var obj = JSON.parse(data);
            alert(data);
            drawFormDriver(obj.userData[0]);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(jqXHR.responseText + " Error!");
        }
    })

}
function drawFormDriver(rowData){
    document.getElementById("idForDriver").value = rowData.userId;
    document.getElementById("firstName").value = rowData.firstName;
    document.getElementById("lastName").value = rowData.lastName;
    document.getElementById("phone").value = rowData.phone;
    document.getElementById("email").value = rowData.email;
    document.getElementById("regpass").value = rowData.getPassword;
}

function addDriver(){
    alert("driver add....");
    var JSONdata = {
        firstName: $("#firstName").val(),
        lastName: $("#lastName").val(),
        phone: $("#phone").val(),
        email: $("#email").val(),
        pass: $("#regpass").val()
    };
    setDriver(JSONdata);
}
function editDriver(){
    alert("driver add....");
    var JSONdata = {
        id: $("#idForDriver").val(),
        firstName: $("#firstName").val(),
        lastName: $("#lastName").val(),
        phone: $("#phone").val(),
        email: $("#email").val(),
        pass: $("#regpass").val()
    };
    setDriver(JSONdata);
}
function setDriver(JSONdata){

    alert(JSON.stringify(JSONdata))
    $.ajax({
        method: 'POST',
        url: "api/user/create_driver",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(JSONdata),
        dataType:'text',
        success: function (data,textStatus,jqXHR ) {
            alert(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("Wrong user credentials.");
        }
    })


}
function deleteDriver(){
    var id = $("#idForDriver").val();
    alert("asdjk");
    var JSONdata = {
        id: $("#idForDriver").val()
    };
    $.ajax({
        method: 'POST',
        url: "api/user/delete",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(JSONdata),
        dataType:'text',
        success: function (data,textStatus,jqXHR ) {
            alert(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("Wrong user credentials.");
        }
    })
}

function addCar(){
    alert("carsdffdjkg");
    var JSONdata = {
        model: $("#model").val(),
        seatCount: $("#seatCount").val(),
        classId: $("#classCar").val(),
        userId: $("#driverId").val(),
        licencePlate: $("#licencePlate").val(),
        requiredDriverCategory: $("#requiredDriverCategory").val(),
        airConditioner: $("#conditioner").val()
    };
    setCar(JSONdata);
}
function editCar(){
    alert("driver add....");
    var JSONdata = {
        id: $("#idForCar").val(),
        model: $("#model").val(),
        seatCount: $("#seatCount").val(),
        classId: $("#classCar").val(),
        userId: $("#driverId").val(),
        licencePlate: $("#licencePlate").val(),
        requiredDriverCategory: $("#requiredDriverCategory").val(),
        airConditioner: $("#conditioner").val()
    };
    setCar(JSONdata);
}
function setCar(JSONdata){
    alert(JSON.stringify(JSONdata))
    $.ajax({
        method: 'POST',
        url: "api/car/create_car",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(JSONdata),
        dataType:'text',
        success: function (data,textStatus,jqXHR ) {
            alert(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("Wrong user credentials.");
        }
    })
}
function deleteCar(){
    var id = $("#idForCar").val();
    alert("asdjk");
    var JSONdata = {
        id: $("#idForCar").val()
    };
    $.ajax({
        method: 'POST',
        url: "api/car/delete",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(JSONdata),
        dataType:'text',
        success: function (data,textStatus,jqXHR ) {
            alert(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("Wrong user credentials.");
        }
    })
}

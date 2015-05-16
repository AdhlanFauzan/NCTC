/**
 * Created by Ann on 09.05.2015.
 */
var myMap;
var myPath;
//var rootURL = "http://localhost:8085/TestMaps_war_exploded/map.html";
//var coord;

// ������� �������� API � ���������� DOM.
ymaps.ready(init);


function init() {
    // �������� ���������� ����� � ��� �������� � ���������� �
    // �������� id ("map").
    myMap = new ymaps.Map('map', {
        // ��� ������������� ����� ����������� ����� �������
        // � ����� � ����������� ���������������.

        center: [50.450097, 30.523397], // ����
        zoom: 10
    });
}

function makeSearch(element) {
    alert(element.value + " " + element.id);
    var myGeocoder = ymaps.geocode(element.value, {
        /**
         * ����� �������
         * @see https://api.yandex.ru/maps/doc/jsapi/2.1/ref/reference/geocode.xml
         */
        // boundedBy: myMap.getBounds(), // ���������� ����������� �� ������ ���� �����
        // strictBounds: true, // ������ � ������ boundedBy ����� ������ ������ ������ �������, ��������� � boundedBy
        results: 1 // ���� ����� ������ ���� ���������, �������� ������ �������������
    });
    myGeocoder.then(function (res) {
        // �������� ������ ��������� ��������������.
        var firstGeoObject = res.geoObjects.get(0);
        // ���������� ����������.
        var coords = firstGeoObject.geometry.getCoordinates();
        // ������� ��������� ����������.
        //var bounds = firstGeoObject.properties.get('boundedBy');
        if (element.id.indexOf("from") == 0) {
            document.getElementById("fromX").value = coords[0];
            document.getElementById("fromY").value = coords[1];
            setLock("#" + element.id);
            setUnlock("#toAddress0");
        }
        else {
            document.getElementById("toX" + element.id.slice(-1)).value = coords[0];
                document.getElementById("toY" + element.id.slice(-1)).value = coords[1];
        }

        // ��������� ������ ��������� ��������� �� �����.
        myMap.geoObjects.add(firstGeoObject);
        // ������������ ����� �� ������� ��������� ����������.
        //myMap.setBounds(bounds, {
        //    checkZoomRange: true // ��������� ������� ������ �� ������ ��������.
        //});
    });
    if (document.getElementById('fromAddress').value != '' && document.getElementById('toAddress0').value != '') {
        buildPath(element.id.slice(-1));
    }
    return false;
}

function buildPath(index) {
    myMap.geoObjects.removeAll();
    myMap.geoObjects.each(function (geoObject) {
        myMap.geoObjects.remove(geoObject);
        return false;
    });
    var pointsArray = [];
    pointsArray.push(document.getElementById('fromAddress').value);

    for(var i = 0; i <= index; i++) {
        pointsArray.push(document.getElementById('toAddress' + i).value);
        alert(pointsArray);
    }
    myPath= new ymaps.route(
        pointsArray
    , {
        // Router options
        mapStateAutoApply: true // automatically position the map
    }).then(function (route) {
        myMap.geoObjects.add(route);
        document.getElementById('totalLength').value=route.getLength();
           setLock("#toAddress"+index);
    }, function (error) {
        alert("An error occurred: " + error.message);
    });
    return false;
}
function setLock(name){
    $(name).prop('disabled', true);
}
function setUnlock(name){
    $(name).prop('disabled', false);
}

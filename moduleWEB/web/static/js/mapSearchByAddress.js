/**
 * Created by Ann on 09.05.2015.
 */
var myMap;
var myPath;
//var rootURL = "http://localhost:8085/TestMaps_war_exploded/map.html";
//var coord;

// ������� �������� API � ���������� DOM.
    ymaps.ready(init);



function init () {
    // �������� ���������� ����� � ��� �������� � ���������� �
    // �������� id ("map").
    myMap = new ymaps.Map('map', {
        // ��� ������������� ����� ����������� ����� �������
        // � ����� � ����������� ���������������.

        center: [50.450097, 30.523397], // ����
        zoom: 10
    });
}

function makeSearch(value, resultx,resulty){
   // var value = document.getElementById("fromAddress0").value;
    ymaps.geocode(value, {
        /**
         * ����� �������
         * @see https://api.yandex.ru/maps/doc/jsapi/2.1/ref/reference/geocode.xml
         */
        // boundedBy: myMap.getBounds(), // ���������� ����������� �� ������ ���� �����
        // strictBounds: true, // ������ � ������ boundedBy ����� ������ ������ ������ �������, ��������� � boundedBy
        results: 1 // ���� ����� ������ ���� ���������, �������� ������ �������������
    }).then(function (res) {
        // �������� ������ ��������� ��������������.
        var firstGeoObject = myMap.geoObjects.get(0),
        // ���������� ����������.
            coords = firstGeoObject.geometry.getCoordinates(),
        // ������� ��������� ����������.
            bounds = firstGeoObject.properties.get('boundedBy');
        resultx.value=parseFloat(coords[0]);
        resulty.value=parseFloat(coords[1]);

        // ��������� ������ ��������� ��������� �� �����.
        myMap.geoObjects.add(firstGeoObject);
        // ������������ ����� �� ������� ��������� ����������.
        myMap.setBounds(bounds, {
            checkZoomRange: true // ��������� ������� ������ �� ������ ��������.
        });
    });
    if (document.getElementById('fromAddress').value!='' && document.getElementById('toAddress0').value!=''){
        buildPath();
    }
    return false;
}

function buildPath(){
    myMap.geoObjects.each(function (geoObject) {
           myMap.geoObjects.remove(geoObject);
            return false;
    });
    var fromPoint=document.getElementById('fromAddress').value;
    var toPoint=document.getElementById('toAddress0').value;
    myPath= new ymaps.route([
        fromPoint,
        toPoint
    ], {
        // Router options
        mapStateAutoApply: true // automatically position the map
    }).then(function (route) {
        myMap.geoObjects.add(route);
        document.getElementById('totalLength').value=route.getLength();
    }, function (error) {
        alert("An error occurred: " + error.message);
    });

    return false;
}

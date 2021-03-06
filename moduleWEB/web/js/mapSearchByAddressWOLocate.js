var myMap;
var myPath;

function init() {
    // �������� ���������� ����� � ��� �������� � ���������� �
    // �������� id ("map").
    var geolocation = ymaps.geolocation;
    myMap = new ymaps.Map('map', {
        // ��� ������������� ����� ����������� ����� �������
        // � ����� � ����������� ���������������.
        center: [50.45, 30.52], // ����
        zoom: 11
    });
}

function makeSearch(element) {
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
        document.getElementById("toX" + element.id.slice(-1)).value = coords[0];
        document.getElementById("toY" + element.id.slice(-1)).value = coords[1];

        // ��������� ������ ��������� ��������� �� �����.
        myMap.geoObjects.add(firstGeoObject);
        // ������������ ����� �� ������� ��������� ����������.
        //myMap.setBounds(bounds, {
        //    checkZoomRange: true // ��������� ������� ������ �� ������ ��������.
        //});
    });
    if (document.getElementById('fromAddress').value != '' && document.getElementById('toAddress0').value != '') {
        if (element.id != "fromAddress") {
            buildPath(element.id.slice(-1));
        } else buildPath(0);
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

    for (var i = 0; i <= index; i++) {
        pointsArray.push(document.getElementById('toAddress' + i).value);
        //alert(pointsArray);
    }
    myPath = new ymaps.route(
        pointsArray
        , {
            // Router options
            mapStateAutoApply: true // automatically position the map
        }).then(function (route) {
            myMap.geoObjects.add(route);
            var Paths = route.getPaths();
            for (var i = 0; i < Paths.getLength(); i++) {
                document.getElementById('distance' + i).value = parseFloat(Paths.get(i).getLength() / 1000).toFixed(2);
                console.log(Paths.get(i).getLength());
            }
            //console.log(Paths);
            document.getElementById('totalLength').value = parseFloat(route.getLength() / 1000).toFixed(2);
            updatePrice();
        }, function (error) {
            alert("Unable to build path. Input correct addresses.");
        });
    return false;
}

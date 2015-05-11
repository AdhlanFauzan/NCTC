/**
 * Created by Ann on 09.05.2015.
 */
var geoResult;
//var rootURL = "http://localhost:8085/TestMaps_war_exploded/map.html";
//var coord;

// ������� �������� API � ���������� DOM.
    ymaps.ready(init);



function init () {
    // �������� ���������� ����� � ��� �������� � ���������� �
    // �������� id ("map").
    var myMap = new ymaps.Map('map', {
        // ��� ������������� ����� ����������� ����� �������
        // � ����� � ����������� ���������������.
        center: [55.76, 37.64], // ������
        zoom: 10
    });

ymaps.geocode('����', {
    /**
     * ����� �������
     * @see https://api.yandex.ru/maps/doc/jsapi/2.1/ref/reference/geocode.xml
     */
    // boundedBy: myMap.getBounds(), // ���������� ����������� �� ������ ���� �����
    // strictBounds: true, // ������ � ������ boundedBy ����� ������ ������ ������ �������, ��������� � boundedBy
    results: 1 // ���� ����� ������ ���� ���������, �������� ������ �������������
}).then(function (res) {
    // �������� ������ ��������� ��������������.
    var firstGeoObject = res.geoObjects.get(0),
    // ���������� ����������.
        coords = firstGeoObject.geometry.getCoordinates(),
    // ������� ��������� ����������.
        bounds = firstGeoObject.properties.get('boundedBy');
    console.log(coords);

    // ��������� ������ ��������� ��������� �� �����.
    myMap.geoObjects.add(firstGeoObject);
    // ������������ ����� �� ������� ��������� ����������.
    myMap.setBounds(bounds, {
        checkZoomRange: true // ��������� ������� ������ �� ������ ��������.
    });

    /**
     * ��� ������ � ���� javascript-�������.
     */
    console.log("er ��� ������ ����������: ", firstGeoObject.properties.getAll());
    /**
     * ���������� ������� � ������ ���������.
     * @see https://api.yandex.ru/maps/doc/geocoder/desc/reference/GeocoderResponseMetaData.xml
     */
    console.log('���������� ������ ���������: ', res.metaData);
    /**
     * ���������� ���������, ������������ ��� ���������� �������.
     * @see https://api.yandex.ru/maps/doc/geocoder/desc/reference/GeocoderMetaData.xml
     */
    console.log('���������� ���������: ', firstGeoObject.properties.get('metaDataProperty.GeocoderMetaData'));
    /**
     * �������� ������ (precision) ������������ ������ ��� �����.
     * @see https://api.yandex.ru/maps/doc/geocoder/desc/reference/precision.xml
     */
    console.log('precision', firstGeoObject.properties.get('metaDataProperty.GeocoderMetaData.precision'));
    /**
     * ��� ���������� ������� (kind).
     * @see https://api.yandex.ru/maps/doc/geocoder/desc/reference/kind.xml
     */
    console.log('��� ����������: %s', firstGeoObject.properties.get('metaDataProperty.GeocoderMetaData.kind'));
    console.log('�������� �������: %s', firstGeoObject.properties.get('name'));
    console.log('�������� �������: %s', firstGeoObject.properties.get('description'));
    console.log('������ �������� �������: %s', firstGeoObject.properties.get('text'));
});
}

function showAddress (value) {
    //var map = document.getElementById("map").id;
    // �������� ����������� ���������� ������
    myMap.removeOverlay(geoResult);

    // ������ �������� ��������������
    var geocoder = new ymaps.Geocoder(value, {results: 1, boundedBy: myMap.getBounds()});

    // �������� ����������� ��� ��������� ���������� ��������������
    YMaps.Events.observe(geocoder, geocoder.Events.Load, function () {
        // ���� ������ ��� ������, �� ��������� ��� �� �����
        // � ���������� ����� �� ������� ������ ���������� �������
        if (this.length()) {
            geoResult = this.get(0);
            myMap.addOverlay(geoResult);
            myMap.setBounds(geoResult.getBounds());
        } else {
            alert("������ �� �������")
        }
    });
}


/**
 * Created by Ann on 09.05.2015.
 */
var geoResult;
var myMap;
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
function makeSearch(value, result){
   // var value = document.getElementById("fromAddress0").value;
    console.log(value);
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
        var firstGeoObject = res.geoObjects.get(0),
        // ���������� ����������.
            coords = firstGeoObject.geometry.getCoordinates(),
        // ������� ��������� ����������.
            bounds = firstGeoObject.properties.get('boundedBy');
        result.value=parseFloat(coords);

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
    return false;
}


<?php
include_once 'BD.php';


// string  request (ie, get/post) global array to a variable
$requestData= $_REQUEST;


$columns = array(
// datatable column index  => database column name
    0 => 'id',
    1 => 'title',
    2 => 'author',
    3 => 'publisher',
    4 => 'edition',
    5 => 'barcode',
    6 => 'createdDate',
    7 => 'customerPrice',
    8 => 'annotation',
    9 => 'imgPath'
);


$sql = "SELECT c.id,b.title,b.author,b.publisher,b.edition,b.barcode,c.createdDate,c.customerPrice,c.annotation,c.imgPath";
$sql.=" FROM book b inner join concession c on b.id = c.idBook";
$sql.= " WHERE c.valide = 4";



// getting records as per search parameters
if (!empty($requestData['search']['value'])) {   //name
    $sql.=" AND (title LIKE '".$requestData['search']['value']."%' ";
    $sql.=" OR author LIKE '".$requestData['search']['value']."%' ";
    $sql.=" OR publisher LIKE '".$requestData['search']['value']."%' ";
    $sql.=" OR id '".$requestData['search']['value']."%' ";
    $sql.=" OR edition LIKE '".$requestData['search']['value']."%' ";
    $sql.=" OR createdDate LIKE '".$requestData['search']['value']."%' ";
    $sql.=" OR annotation LIKE '".$requestData['search']['value']."%' ";
    $sql.=" OR customerPrice LIKE '".$requestData['search']['value']."%' ";
    $sql.=" OR barcode LIKE '".$requestData['search']['value']."%' ) ";
}

$stmt = $conn->prepare($sql);
$stmt->execute();
$totalData = $stmt->rowCount();
$totalFiltered = $totalData;  // when there is no search parameter then total number rows = total number filtered rows.


$dir = 'asc';

if ($requestData['order'][0]['dir'] != 'asc') {
    $dir = 'desc';
}

$sql.=" ORDER BY ". $columns[$requestData['order'][0]['column']] . " " . $dir;
$sql.="  LIMIT ".$requestData['start']." ,".$requestData['length']."   ";



$stmt = $conn->prepare($sql);
$stmt->execute();
$totalData = $stmt->rowCount();


$data = [];

$result = $stmt->fetchAll();


$json_data = array(
        "draw"            => intval($requestData['draw']),   // for every request/draw by clientside , they send a number as a parameter, when they recieve a response/data they first check the draw number, so we are sending same number in draw.
        "recordsTotal"    => intval($totalData),  // total number of records
        "recordsFiltered" => intval($totalFiltered),
        "data"            => $result   // total data array
        );

echo json_encode($json_data);  // send data as json format

$conn = null;

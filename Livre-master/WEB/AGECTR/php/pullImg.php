<?php
include_once 'BD.php';


// string  request (ie, get/post) global array to a variable
$requestData= $_REQUEST;


$columns = array(
// datatable column index  => database column name
    0 => 'title',
    1 => 'author',
    2 => 'publisher',
    3 => 'edition',
    4 => 'barcode',
    5 => 'createdDate',
    6 => 'imgPath'
);


$sql = "SELECT title,author,publisher,edition,barcode,createdDate,imgPath,id";
$sql.=" FROM book";
$sql.= " WHERE 1 = 1";



// getting records as per search parameters
if (!empty($requestData['search']['value'])) {   //name
    $sql.=" AND (title LIKE '".$requestData['search']['value']."%' ";
    $sql.=" OR author LIKE '".$requestData['search']['value']."%' ";
    $sql.=" OR publisher LIKE '".$requestData['search']['value']."%' ";
    $sql.=" OR edition LIKE '".$requestData['search']['value']."%' ";
    $sql.=" OR createdDate LIKE '".$requestData['search']['value']."%' ";
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

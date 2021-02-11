<?php
include_once 'BD.php';


// string  request (ie, get/post) global array to a variable
$requestData= $_REQUEST;


$columns = array(
// datatable column index  => database column name
    0 => 'referenceCode',
    1 => 'firstName',
    2 => 'lastName',
    3 => 'phoneNumber',
    4 => 'email',
    5 => 'createdDate',
    6 => 'inactive'
);


$sql = "SELECT referenceCode,firstName,lastName,phoneNumber,email,createdDate,inactive,id";
$sql.=" FROM customer";
$sql.= " WHERE (email = 'N/A' OR password = '') ";



// getting records as per search parameters
if (!empty($requestData['search']['value'])) {   //name
    $sql.=" AND (referenceCode LIKE '".$requestData['search']['value']."%' ";
    $sql.=" OR email LIKE '".$requestData['search']['value']."%' ";
    $sql.=" OR phoneNumber LIKE '".$requestData['search']['value']."%' ";
    $sql.=" OR firstName LIKE '".$requestData['search']['value']."%' ";
    $sql.=" OR createdDate LIKE '".$requestData['search']['value']."%' ";
    $sql.=" OR lastName LIKE '".$requestData['search']['value']."%' ) ";
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

//var_dump($requestData['order']);

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

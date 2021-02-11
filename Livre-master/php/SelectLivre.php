<?php

require 'BD.inc.php';

if (isset($_POST['barcode'])) {
    $sql = "SELECT title,author,publisher,edition from book where barcode=:barcode;";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':barcode' => $_POST['barcode']);
    $livre = $stmt->fetch();

    if ($livre[0]!="")
        echo json_encode($livre);
    else 
        echo json_encode('error');
} else {
    echo json_encode('error');
}


$conn = null;

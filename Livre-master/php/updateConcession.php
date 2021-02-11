<?php

require 'BD.php';

if(isset($_POST['id']) && isset($_POST['prix']) && isset($_POST['annotation']) && isset($_POST['imgpath'])){

$sql = "UPDATE concession SET prix = :prix, annotation = :annotation, imgPath = :imgPath where id = :id;";
$stmt = $conn->prepare($sql);
$stmt->execute(array(':id'=>$_POST['id'], ':prix' => $_POST['prix'], ':annotation' => $_POST['annotation'], ':imgPath' => $_POST['imgPath']));
echo 'success';

}
echo 'champ';

$conn = null;

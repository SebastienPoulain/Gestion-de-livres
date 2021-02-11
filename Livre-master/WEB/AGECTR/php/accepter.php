<?php

include "BD.php";

if (isset($_POST["id"])) {
    $id = $_POST["id"];
    $inactive = 0;

    $sql = "UPDATE customer set inactive = :inactive where id = :id";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':inactive' => $inactive,':id' => $id));
    echo "success";
} else {
    echo "error";
}
$conn = null;

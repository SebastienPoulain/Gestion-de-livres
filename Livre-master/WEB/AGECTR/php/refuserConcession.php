<?php

include "BD.php";

if (isset($_POST["id"])) {
    $id = $_POST["id"];

    $sql = "DELETE from concession where id = :id";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':id' => $id));
    echo "success";
} else {
    echo "error";
}
$conn = null;

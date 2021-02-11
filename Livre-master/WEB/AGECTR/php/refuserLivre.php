<?php

include "BD.php";

if (isset($_POST["id"])) {
    $id = $_POST["id"];


        $sql = "SELECT id from concession where idBook = :id";
        $stmt = $conn->prepare($sql);
        $stmt->execute(array(':id' => $id));
        $concession = $stmt->fetch(PDO::FETCH_OBJ);

        $sql = "DELETE from concession where id = :id";
        $stmt = $conn->prepare($sql);
        $stmt->execute(array(':id' => $concession->id));

    $sql = "DELETE from book where id = :id";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':id' => $id));


    echo "success";
} else {
    echo "error";
}
$conn = null;

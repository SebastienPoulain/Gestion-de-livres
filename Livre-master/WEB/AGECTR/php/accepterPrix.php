<?php

include "BD.php";

if (isset($_POST["id"])) {
    $id = $_POST["id"];
    $valide = 1;
    $prix = $_POST["prix"];
    $ancien = $_POST["ancien"];

    $sql = "UPDATE concession set customerPrice = :customerPrice where id = :id";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':customerPrice' => $prix,':id' => $id));


    $sql = "SELECT feesPercentage from concession  where id = :id";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':id' => $id));
    $taux = $stmt->fetch(PDO::FETCH_OBJ);

    $tauxFees = $taux->feesPercentage;

    $prixFinal = $prix + ($prix * ($tauxFees/100));

    $sql = "UPDATE concession set sellingPrice = :sellingPrice where id = :id";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':sellingPrice' => $prixFinal,':id' => $id));

    $sql = "UPDATE concession set valide = :valide where id = :id";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':valide' => $valide,':id' => $id));
    echo "success";
} else {
    echo "error";
}
$conn = null;

<?php
require_once 'dbconnect.php';

//fetch table rows from mysql db
    $sql = "select * from proj";
    $result = mysqli_query($con1, $sql) or die("Error in Selecting " . mysqli_error($connection));
    //create an array
    $emparray = array();
    while($row =mysqli_fetch_assoc($result))
    {
        $emparray[] = $row;
    }
echo json_encode($emparray);
?>
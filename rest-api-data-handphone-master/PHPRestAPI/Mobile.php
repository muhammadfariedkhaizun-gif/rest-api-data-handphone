<?php
require_once("dbcontroller.php");
/* 
A domain Class to demonstrate RESTful web services
*/
Class Mobile {
	private $mobiles = array();
	/*
		you should hookup the DAO here
	*/
	public function getAllMobile(){
		$query = "SELECT * FROM tbl_mobile";
		$dbcontroller = new DBController();
		$this->mobiles = $dbcontroller->executeSelectQuery($query);
		return $this->mobiles;
	}	
}
?>
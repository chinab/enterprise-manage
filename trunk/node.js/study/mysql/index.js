var mysql = require('mysql');
var pool = mysql.createPool({
	host     : 'localhost',
	user     : 'artogrid',
	password : 'artogrid',
});

pool.getConnection(function(err, connection){
	connection.query('SELECT 1 + 1 AS solution', function(err, rows, fields) {
		if (err) throw err;
		console.log('The solution is: ', rows[0].solution);
		connection.end();
	});
});
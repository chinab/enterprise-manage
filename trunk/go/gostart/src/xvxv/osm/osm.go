package osm

import (
	"database/sql"
	"fmt"
	"reflect"
)

type Osm struct {
	db            *sql.DB
	sqlMappersMap map[string]*sqlMapper
}

func NewOsm(driverName, dataSource string, xmlPaths []string, params ...int) (osm *Osm, err error) {
	osm = new(Osm)
	db, err := sql.Open(driverName, dataSource)
	if err != nil {
		if db != nil {
			db.Close()
		}
		err = fmt.Errorf("create osm %s", err.Error())
		return
	}

	osm.db = db
	osm.sqlMappersMap = make(map[string]*sqlMapper)

	for i, v := range params {
		switch i {
		case 0:
			osm.db.SetMaxIdleConns(v)
		case 1:
			osm.db.SetMaxOpenConns(v)
		}
	}

	for _, xmlPath := range xmlPaths {
		sqlMappers, err := readMappers(xmlPath)
		if err == nil {
			for _, sm := range sqlMappers {
				osm.sqlMappersMap[sm.id] = sm
			}
		} else {
			err = fmt.Errorf("read sqlMappers %s", err.Error())
		}
	}

	return
}

func (o *Osm) Query(id string, param interface{}) (err error) {
	t := reflect.TypeOf(param)
	t

	sm, ok := o.sqlMappersMap[id]
	if !ok {
		return fmt.Errorf("Query \"%s\" error ,id not fond ", id)
	}
	var params []interface{}
	for _, paramName := range sm.paramNames {
		params = append(params, paramMap[paramName])
	}
	stmt, err := o.db.Prepare(sm.sql)
	rows, err := stmt.Query(params...)

	for rows.Next() {
		var uid int
		var username string
		var department string
		var created string
		err = rows.Scan(&uid, &username, &department, &created)

		fmt.Println(uid)
		fmt.Println(username)
		fmt.Println(department)
		fmt.Println(created)
	}
	return
}

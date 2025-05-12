db.createUser({
    user: "poc-user",
    pwd: "qwerty",
    roles: [{ role: "readWrite", db: "tablePlacement" }]
});

var commonTableInfo = {
    "capacity": 4,
    "isAvailable": false,
    "menuOrderList": [],
    "_class": "com.demo.poc.business.tableplacement.domain.repository.tableorder.document.TableDocument"
};

var tableList = [];
for (var i = 1; i <= 6; i++) {
    var table = Object.assign({}, commonTableInfo);
    table.tableNumber = i;
    tableList.push(table);
    //menuOrderList: Array empty
}

db.tables.insert(tableList);
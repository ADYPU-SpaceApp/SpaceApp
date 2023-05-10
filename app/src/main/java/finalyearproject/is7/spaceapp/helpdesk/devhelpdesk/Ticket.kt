package finalyearproject.is7.spaceapp.helpdesk.devhelpdesk

class Ticket(var id: String?, var description: String?, var status: String?, var user: String?, var timestamp: Long) {

    constructor() : this("","", "", "", 0)

}

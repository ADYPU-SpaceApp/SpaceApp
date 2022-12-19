package finalyearproject.is7.spaceapp

class Message(var message: String?, var timestamp: Long, var senderId: String?) {

    constructor() : this("", 0, "")

}
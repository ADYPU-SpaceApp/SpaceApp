package finalyearproject.is7.spaceapp.chatting

class Message(var message: String?, var timestamp: Long, var senderId: String?) {

    constructor() : this("", 0, "")

}
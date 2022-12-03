package finalyearproject.is7.spaceapp

class User {

    var email: String? = null
    var name: String? = null
    var uid: String? = null

    constructor()

    constructor(email: String?, name: String?, uid: String?){
        this.email = email
        this.name = name
        this.uid = uid
    }

}
package finalyearproject.is7.spaceapp

import com.google.firebase.firestore.FirebaseFirestore

class SpaceAppModule {

    // TODO: create SpaceAppModule to eliminate the need for a repeated call to FirebaseFirestore.getInstance() and repeated lines of code

    val mDb = FirebaseFirestore.getInstance()

    fun getType(uid: String): String {
//        mDb.collection("Dev").document(uid).get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    return@addOnSuccessListener "Dev"
//                }
//            }
        return "Error"
    }

    fun getUserRole(uid: String): String {
        return "Error"
    }

    fun checkIsStaff(uid: String): Boolean {
        return false
    }

}

package io.spaceapps.firebase_messenger.models

class User(val uid: String, val username: String, val profileImageUrl: String) {
    constructor() : this("", "", "")
}

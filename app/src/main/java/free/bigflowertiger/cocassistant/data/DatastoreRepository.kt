package free.bigflowertiger.cocassistant.data

import com.dylanc.datastore.DataStoreOwner

object DatastoreRepository : DataStoreOwner(name = "settings") {
    val selectedAlarmAppId by stringPreference(null)
}
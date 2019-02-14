package me.urielsalis.youtubebot.infrastructure.db

import me.urielsalis.youtubebot.domain.repository.CommandRepository
import org.mapdb.DBMaker
import org.mapdb.Serializer

class MapDBCommandRepository : CommandRepository {
    override fun save(key: String, value: String) {
        val db = DBMaker.fileDB("commands.db").fileMmapEnable().make()
        val map = db.hashMap("commands", Serializer.STRING, Serializer.STRING).createOrOpen()
        map[key] = value
        db.close()
    }

    override fun findByKey(key: String): String? {
        val db = DBMaker.fileDB("commands.db").fileMmapEnable().make()
        val map = db.hashMap("commands", Serializer.STRING, Serializer.STRING).createOrOpen()
        return map[key].also {
            db.close()
        }
    }
}
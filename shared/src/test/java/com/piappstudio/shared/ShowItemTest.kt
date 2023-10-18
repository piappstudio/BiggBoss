package com.piappstudio.shared
import junit.framework.TestCase.assertNotNull
import kotlinx.serialization.Serializable
import model.ShowItem
import org.junit.Test
import java.io.ObjectOutputStream

@Serializable
class Show(val title:String)
@Serializable
data class Customer(val id: Int, val firstName: String, val lastName: String)
class ShowItemTest {
    @Test
    fun testSerialization() {
        val showItem = Customer(id =1, firstName = "Bigg Boss Tamil 7", lastName = "")
        // Serialization
        val byteArray = java.io.ByteArrayOutputStream().use {
            ObjectOutputStream(it).writeObject(showItem)
            it.toByteArray()
        }

        // Deserialization
        val deserializedPerson = java.io.ByteArrayInputStream(byteArray).use {
            java.io.ObjectInputStream(it).readObject() as ShowItem
        }
        assertNotNull(deserializedPerson)


    }
}
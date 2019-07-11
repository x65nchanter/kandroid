package org.x65nchanter.kandroid

import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class KandroidApplicationTests {

    private val jsonContentType = MediaType(MediaType.APPLICATION_JSON.type, MediaType.APPLICATION_JSON.subtype)
    private val baseURL = "http://localhost:8000"
    private val baseURLBoard = baseURL + "/board"
    private val emptyJsonResponse = "[]"

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var webAppContext: WebApplicationContext

    @Before
    fun before() {
        mockMvc = webAppContextSetup(webAppContext).build()
    }

    private fun routineTestRouteExist(base: String) {
        val request = get(base).contentType(jsonContentType)

        val response = mockMvc.perform(request)
        response.andExpect(status().isOk).andExpect(content().string(emptyJsonResponse))
    }

    private fun routineTestPostOne(base: String, passedJsonString: String, resultJsonString: String) {
        val request = post(base).contentType(jsonContentType).content(passedJsonString)


        val response = mockMvc.perform(request)
        response.andExpect(status().isCreated)
                .andExpect(content().json(resultJsonString, true))
    }
//
//    fun `3 - Update first product`() {
//        val passedJsonString = """
//            {
//                "name": "iPhone 4S",
//                "description": "Smart phone by Apple"
//            }
//        """.trimIndent()
//
//        val request = put(baseUrl + "1").contentType(jsonContentType).content(passedJsonString)
//
//        val resultJsonString = """
//            {
//                "name": "iPhone 4S",
//                "description": "Smart phone by Apple",
//                "id": 1
//            }
//        """.trimIndent()
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk)
//                .andExpect(content().json(resultJsonString, true))
//    }
//
//    fun `4 - Get first product`() {
//        val request = get(baseUrl + "1").contentType(jsonContentType)
//
//        val resultJsonString = """
//            {
//                "name": "iPhone 4S",
//                "description": "Smart phone by Apple",
//                "id": 1
//            }
//        """.trimIndent()
//
//        mockMvc.perform(request)
//                .andExpect(status().isFound)
//                .andExpect(content().json(resultJsonString, true))
//    }
//
//    fun `5 - Get list of products, with one product`() {
//        val request = get(baseUrl).contentType(jsonContentType)
//
//        val resultJsonString = """
//            [
//                {
//                    "name": "iPhone 4S",
//                    "description": "Smart phone by Apple",
//                    "id": 1
//                }
//            ]
//        """.trimIndent()
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk)
//                .andExpect(content().json(resultJsonString, true))
//    }
//
//    fun `6 - Delete first product`() {
//        val request = delete(baseUrl + "1").contentType(jsonContentType)
//
//        mockMvc.perform(request).andExpect(status().isOk)
//    }

	@Test
    fun `01 - test exist board route`() = routineTestRouteExist(baseURLBoard)

    @Test
    fun `02 - test post one board`() {
        val passedJsonString = """
            {
                "name": "testBoard",
                "description": "testBoardDescription"
            }
        """.trimIndent()

        val resultJsonString = """
            {
                "name": "testBoard",
                "description": "testBoardDescription",
                "startAt": null,
                "endAt":null,
                "id": 1
            }
        """.trimIndent()


        routineTestPostOne(baseURLBoard, passedJsonString, resultJsonString)
    }

	//TODO: Тесты CRUD маршрутов board, column, task
	//TODO: Тесты маршрутов task/assign  и board/assign 
	//TODO: Тесты маршрутов task/promot	
}

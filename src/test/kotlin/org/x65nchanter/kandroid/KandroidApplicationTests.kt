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
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class KandroidApplicationTests {

    private val jsonContentType = MediaType(MediaType.APPLICATION_JSON.type, MediaType.APPLICATION_JSON.subtype)
    private val baseURL = "http://localhost:8000/"
    private val baseURLBoard = baseURL + "board/"
    private val baseURLFirstBoard = baseURLBoard + "1"
    private val emptyJsonResponse = "[]"

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var webAppContext: WebApplicationContext

    @Before
    fun before() {
        mockMvc = webAppContextSetup(webAppContext).build()
    }

    private fun makeResponse(request: RequestBuilder, status: ResultMatcher, content: ResultMatcher? = null): ResultActions {
        val actions = mockMvc.perform(request).andExpect(status)
        if (content != null) {
            actions.andExpect(content)
        }
        return actions
    }

    private fun makeResponseStatusOk(request: RequestBuilder, response: String? = null) =
            makeResponse(request, status().isOk, if (response != null) content().json(response) else null)

    private fun makeResponseStatusCreated(request: RequestBuilder, response: String? = null) =
            makeResponse(request, status().isCreated, if (response != null) content().json(response) else null)

    private fun makeResponseStatusFound(request: RequestBuilder, response: String? = null) =
            makeResponse(request, status().isFound, if (response != null) content().json(response) else null)

    private fun routineTestGetOne(base: String, resultJsonString: String) {
        val request = get(base).contentType(jsonContentType)

        val response = makeResponseStatusFound(request, resultJsonString)
    }

    private fun routineTestRouteGetAll(base: String, result: String) {
        val request = get(base).contentType(jsonContentType)

        val response = makeResponseStatusOk(request, result)
    }

    private fun routineTestRouteGetEmpty(base: String) = routineTestRouteGetAll(base, emptyJsonResponse)

    private fun routineTestPostOne(base: String, passedJsonString: String, resultJsonString: String) {
        val request = post(base).contentType(jsonContentType).content(passedJsonString)

        val response = makeResponseStatusCreated(request, resultJsonString)
    }

    private fun routineTestPut(base: String, passedJsonString: String, resultJsonString: String) {
        val request = put(base).contentType(jsonContentType).content(passedJsonString)

        val response = makeResponseStatusOk(request, resultJsonString)
    }

    private fun routineTestDeleteOne(base: String) {
        val request = delete(base).contentType(jsonContentType)

        val response = makeResponseStatusOk(request)
    }

	@Test
    fun `01 - Exist board route`() = routineTestRouteGetEmpty(baseURLBoard)

    @Test
    fun `02 - Post one board`() {
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
                "status":0,
                "id": 1
            }
        """.trimIndent()


        routineTestPostOne(baseURLBoard, passedJsonString, resultJsonString)
    }

    @Test
    fun `03 - Update first board`() {
        val passedJsonString = """
            {
                "name": "testBoardChangedName",
                "description": "testBoardChangedDescription"
            }
        """.trimIndent()

        val resultJsonString = """
            {
                "name": "testBoardChangedName",
                "description": "testBoardChangedDescription",
                "startAt": null,
                "endAt":null,
                "status":0,
                "id": 1
            }
        """.trimIndent()

        routineTestPut(baseURLFirstBoard, passedJsonString, resultJsonString)
    }

    @Test
    fun `04 - Find first board`() {
        val resultJsonString = """
            {
                "name": "testBoardChangedName",
                "description": "testBoardChangedDescription",
                "startAt": null,
                "endAt":null,
                "status":0,
                "id": 1
            }
        """.trimIndent()

        routineTestGetOne(baseURLFirstBoard, resultJsonString)
    }

    @Test
    fun `05 - Get list of boards, with one board`() {
        val resultJsonString = """
            [{
                "name": "testBoardChangedName",
                "description": "testBoardChangedDescription",
                "startAt": null,
                "endAt":null,
                "status":0,
                "id": 1
            }]
        """.trimIndent()

        routineTestRouteGetAll(baseURLBoard, resultJsonString)
    }

    @Test
    fun `06 - Delete first board`() {
        routineTestDeleteOne(baseURLFirstBoard)
    }

	//TODO: Тесты маршрутов task/assign  и board/assign 
	//TODO: Тесты маршрутов task/promot	
}

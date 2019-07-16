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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
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

    private val baseURLColumn = baseURL + "column/"
    private val baseURLFirstColumn = baseURLColumn + "2"

    private val baseURLTask = baseURL + "task/"
    private val baseURLFirstTask = baseURLTask + "3"

    private val emptyJsonResponse = "[]"

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var webAppContext: WebApplicationContext

    @Before
    fun before() {
        mockMvc = webAppContextSetup(webAppContext).build()
    }

    private fun makeResponse(request: RequestBuilder, status: ResultMatcher, response: String? = null): ResultActions {
        val actions = mockMvc.perform(request).andExpect(status)
        if (response != null) {
            actions.andExpect(content().json(response))
        }
        return actions
    }

    private fun makeResponseStatusOk(request: RequestBuilder, response: String? = null) =
            makeResponse(request, status().isOk, response)

    private fun makeResponseStatusCreated(request: RequestBuilder, response: String? = null) =
            makeResponse(request, status().isCreated, response)

    private fun makeResponseStatusFound(request: RequestBuilder, response: String? = null) =
            makeResponse(request, status().isFound, response)

    private fun routineTestGetOne(base: String, resultJsonString: String) {
        val request = get(base).contentType(jsonContentType)

        val response = makeResponseStatusFound(request, resultJsonString)
    }

    private fun routineTestGetAll(base: String, result: String) {
        val request = get(base).contentType(jsonContentType)

        val response = makeResponseStatusOk(request, result)
    }

    private fun routineTestGetEmpty(base: String) = routineTestGetAll(base, emptyJsonResponse)

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
    fun `01 - Exist board route`() = routineTestGetEmpty(baseURLBoard)

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

        routineTestGetAll(baseURLBoard, resultJsonString)
    }

    @Test
    fun `06 - Delete first board`() = routineTestDeleteOne(baseURLFirstBoard)

    @Test
    fun `11 - Exist column route`() = routineTestGetEmpty(baseURLColumn)

    @Test
    fun `12 - Post one column`() {
        val passedJsonString = """
            {
                "name": "testColumn",
                "order": 1
            }
        """.trimIndent()

        val resultJsonString = """
            {
                "name": "testColumn",
                "order": 1,
                "id": 2
            }
        """.trimIndent()


        routineTestPostOne(baseURLColumn, passedJsonString, resultJsonString)
    }

    @Test
    fun `13 - Update first column`() {
        val passedJsonString = """
            {
                "name": "testColumnChangedName",
                "order": 2
            }
        """.trimIndent()

        val resultJsonString = """
            {
                "name": "testColumnChangedName",
                "order": 2,
                "id": 2
            }
        """.trimIndent()

        routineTestPut(baseURLFirstColumn, passedJsonString, resultJsonString)
    }

    @Test
    fun `14 - Find first column`() {
        val resultJsonString = """
            {
                "name": "testColumnChangedName",
                "order": 2,
                "id": 2
            }
        """.trimIndent()

        routineTestGetOne(baseURLFirstColumn, resultJsonString)
    }

    @Test
    fun `15 - Get list of columns, with one column`() {
        val resultJsonString = """
            [{
                "name": "testColumnChangedName",
                "order": 2,
                "id": 2
            }]
        """.trimIndent()

        routineTestGetAll(baseURLColumn, resultJsonString)
    }

    @Test
    fun `16 - Delete first column`() = routineTestDeleteOne(baseURLFirstColumn)

    @Test
    fun `21 - Exist task route`() = routineTestGetEmpty(baseURLTask)

    @Test
    fun `22 - Post one task`() {
        val passedJsonString = """
            {
                "name": "testTask",
                "description": "testTaskDescription",
                "priority" : 0
            }
        """.trimIndent()

        val resultJsonString = """
            {
                "name": "testTask",
                "description": "testTaskDescription",
                "priority" : 0,
                "promotAt": null,
                "endAt": null,
                "complexity": null,
                "id": 3
            }
        """.trimIndent()


        routineTestPostOne(baseURLTask, passedJsonString, resultJsonString)
    }

    @Test
    fun `23 - Update first task`() {
        val passedJsonString = """
            {
                "name": "testTaskChangedName",
                "description": "testTaskChangedDescription",
                "priority" : 1
            }
        """.trimIndent()

        val resultJsonString = """
            {
                "name": "testTaskChangedName",
                "description": "testTaskChangedDescription",
                "priority" : 1,
                "promotAt": null,
                "endAt": null,
                "complexity": null,
                "id": 3
            }
        """.trimIndent()

        routineTestPut(baseURLFirstTask, passedJsonString, resultJsonString)
    }

    @Test
    fun `24 - Find first task`() {
        val resultJsonString = """
            {
                "name": "testTaskChangedName",
                "description": "testTaskChangedDescription",
                "priority" : 1,
                "id": 3
            }
        """.trimIndent()

        routineTestGetOne(baseURLFirstTask, resultJsonString)
    }

    @Test
    fun `25 - Get list of tasks, with one task`() {
        val resultJsonString = """
            [{
                "name": "testTaskChangedName",
                "description": "testTaskChangedDescription",
                "priority" : 1,
                "promotAt": null,
                "endAt": null,
                "complexity": null,
                "id": 3
            }]
        """.trimIndent()

        routineTestGetAll(baseURLTask, resultJsonString)
    }

    @Test
    fun `26 - Delete first task`() = routineTestDeleteOne(baseURLFirstTask)

	//TODO: Тесты маршрутов task/assign  и board/assign 
	//TODO: Тесты маршрутов task/promot	
}

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


    private fun makeRequestGet(base: String) = get(base).contentType(jsonContentType)

    private fun makeRequestDelete(base: String) = delete(base).contentType(jsonContentType)

    private fun makeRequestPost(base: String, content: String) =
            post(base).contentType(jsonContentType).content(content)

    private fun makeRequestPut(base: String, content: String) =
            put(base).contentType(jsonContentType).content(content)


    private fun makeResponse(request: RequestBuilder) = mockMvc.perform(request)


    private fun makeResponseStatusOk(request: RequestBuilder) =
            makeResponse(request).andExpect(status().isOk)

    private fun makeResponseStatusCreated(request: RequestBuilder) =
            makeResponse(request).andExpect(status().isCreated)

    private fun makeResponseStatusFound(request: RequestBuilder) =
            makeResponse(request).andExpect(status().isFound)


    private fun routineTestCreate(base: String, passedJsonString: String, resultJsonString: String) =
            makeResponseStatusCreated(makeRequestPost(base, passedJsonString))
                    .andExpect(content().json(resultJsonString))

    private fun routineTestRead(base: String, resultJsonString: String) =
            makeResponseStatusFound(makeRequestGet(base))
                    .andExpect(content().json(resultJsonString))

    private fun routineTestUpdate(base: String, passedJsonString: String, resultJsonString: String) =
            makeResponseStatusOk(makeRequestPut(base, passedJsonString))
                    .andExpect(content().json(resultJsonString))

    private fun routineTestDelete(base: String) =
            makeResponseStatusOk(makeRequestDelete(base))

    private fun routineTestIndex(base: String, resultJsonString: String = emptyJsonResponse) =
            makeResponseStatusOk(makeRequestGet(base))
                    .andExpect(content().json(resultJsonString))

	@Test
    fun `01 - Exist board route`() {
        routineTestIndex(baseURLBoard)
    }

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
                "columns":[],
                "id": 1
            }
        """.trimIndent()


        routineTestCreate(baseURLBoard, passedJsonString, resultJsonString)
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
                "columns":[],
                "id": 1
            }
        """.trimIndent()

        routineTestUpdate(baseURLFirstBoard, passedJsonString, resultJsonString)
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
                "columns":[],
                "id": 1
            }
        """.trimIndent()

        routineTestRead(baseURLFirstBoard, resultJsonString)
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
                "columns":[],
                "id": 1
            }]
        """.trimIndent()

        routineTestIndex(baseURLBoard, resultJsonString)
    }

    @Test
    fun `06 - Delete first board`() {
        routineTestDelete(baseURLFirstBoard)
    }

    @Test
    fun `11 - Exist column route`() {
        routineTestIndex(baseURLColumn)
    }

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
                "tasks": [],
                "overflow": 0,
                "id": 2
            }
        """.trimIndent()


        routineTestCreate(baseURLColumn, passedJsonString, resultJsonString)
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
                "tasks": [],
                "overflow": 0,
                "id": 2
            }
        """.trimIndent()

        routineTestUpdate(baseURLFirstColumn, passedJsonString, resultJsonString)
    }

    @Test
    fun `14 - Find first column`() {
        val resultJsonString = """
            {
                "name": "testColumnChangedName",
                "order": 2,
                "tasks": [],
                "overflow": 0,
                "id": 2
            }
        """.trimIndent()

        routineTestRead(baseURLFirstColumn, resultJsonString)
    }

    @Test
    fun `15 - Get list of columns, with one column`() {
        val resultJsonString = """
            [{
                "name": "testColumnChangedName",
                "order": 2,
                "tasks": [],
                "overflow": 0,
                "id": 2
            }]
        """.trimIndent()

        routineTestIndex(baseURLColumn, resultJsonString)
    }

    @Test
    fun `16 - Delete first column`() {
        routineTestDelete(baseURLFirstColumn)
    }

    @Test
    fun `21 - Exist task route`() {
        routineTestIndex(baseURLTask)
    }

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


        routineTestCreate(baseURLTask, passedJsonString, resultJsonString)
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

        routineTestUpdate(baseURLFirstTask, passedJsonString, resultJsonString)
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

        routineTestRead(baseURLFirstTask, resultJsonString)
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

        routineTestIndex(baseURLTask, resultJsonString)
    }

    @Test
    fun `26 - Delete first task`() {
        routineTestDelete(baseURLFirstTask)
    }

	//TODO: Тесты маршрутов task/assign  и board/assign 
	//TODO: Тесты маршрутов task/promot	
}

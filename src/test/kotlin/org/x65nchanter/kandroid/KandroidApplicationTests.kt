package org.x65nchanter.kandroid

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
import org.springframework.web.context.WebApplicationContext
import org.x65nchanter.kandroid.data.Board
import org.x65nchanter.kandroid.data.Column
import org.x65nchanter.kandroid.data.Task

@RunWith(SpringRunner::class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class KandroidApplicationTests {


    private val jsonContentType = MediaType(MediaType.APPLICATION_JSON.type, MediaType.APPLICATION_JSON.subtype)
    private val mapper = jacksonObjectMapper()

    private val baseURL = "http://localhost:8000/"

    private val baseURLBoard = baseURL + "board/"

    private val baseURLColumn = baseURL + "column/"

    private val baseURLTask = baseURL + "task/"

    private val emptyJsonResponse = "[]"

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var webAppContext: WebApplicationContext

    @Before
    fun before() {
        mockMvc = webAppContextSetup(webAppContext).build()
    }


    private fun makeResponse(request: MockHttpServletRequestBuilder) = mockMvc.perform(request)

    private fun makeRequestGet(base: String) =
            get(base).contentType(jsonContentType)

    private fun makeRequestDelete(base: String) =
            delete(base).contentType(jsonContentType)

    private fun makeRequestPost(base: String, content: String) =
            post(base).contentType(jsonContentType).content(content)

    private fun makeRequestPut(base: String, content: String) =
            put(base).contentType(jsonContentType).content(content)

    private fun routineTestCreate(base: String, passedJsonString: String, resultJsonString: String): ResultActions {
        val request = makeRequestPost(base, passedJsonString)
        return makeResponse(request)
                .andExpect(status().isCreated)
                .andExpect(content().json(resultJsonString))
    }

    private fun routineTestRead(base: String, resultJsonString: String): ResultActions {
        val request = makeRequestGet(base)
        return makeResponse(request)
                .andExpect(status().isFound)
                .andExpect(content().json(resultJsonString))
    }

    private fun routineTestUpdate(base: String, passedJsonString: String, resultJsonString: String): ResultActions {
        val request = makeRequestPut(base, passedJsonString)
        return makeResponse(request)
                .andExpect(status().isOk)
                .andExpect(content().json(resultJsonString))
    }

    private fun routineTestDelete(base: String): ResultActions {
        val request = makeRequestDelete(base)
        return makeResponse(request)
                .andExpect(status().isOk)
    }

    private fun routineTestIndex(base: String, resultJsonString: String? = null): ResultActions {
        val request = makeRequestGet(base)
        val matchOneOrEmpty =
                resultJsonString?.let {
                    jsonPath("$[-1]", it).exists()
                }
                        ?: content().json(emptyJsonResponse)
        return makeResponse(request)
                .andExpect(status().isOk)
                .andExpect(matchOneOrEmpty)
    }

	@Test
    fun `001 - Exist board route`() {
        routineTestIndex(baseURLBoard)
    }

    @Test
    fun `002 - Post one board`() {
        val passedJsonString = """
            {
                "name": "testingBoard",
                "description": "testingBoardDescription"
            }
        """

        val resultJsonString = """
            {
                "name": "testingBoard",
                "description": "testingBoardDescription",
                "startAt": null,
                "endAt":null,
                "status":"PLANING"
            }
        """.trimIndent()


        routineTestCreate(baseURLBoard, passedJsonString, resultJsonString)
    }

    private fun <T> getObjectRequest(url: String, jsonContent: String, valueType: Class<T>): T {
        val createRequest = makeRequestPost(url, jsonContent)
        val responseString = makeResponse(createRequest).andReturn().response.contentAsString
        return mapper.readValue(responseString, valueType)
    }

    //TODO: Искать сущьность перед созданием
    private fun createBoard() = getObjectRequest(
            baseURLBoard,
            """
                {
                    "name": "testingBoard",
                    "description": "testingBoardDescription"
                }
            """,
            Board::class.java)

    @Test
    fun `003 - Update board by id`() {
        val createdBoard = createBoard()
        val passedJsonString = """
            {
                "name": "testBoardChangedName",
                "description": "testBoardChangedNameDescription"
            }
        """.trimIndent()

        val resultJsonString = """
            {
                "name": "testBoardChangedName",
                "description": "testBoardChangedNameDescription",
                "id": ${createdBoard.id}
            }
        """.trimIndent()

        routineTestUpdate(baseURLBoard + createdBoard.id, passedJsonString, resultJsonString)
    }


    @Test
    fun `004 - Find board by id`() {
        val createdBoard = createBoard()
        val resultJsonString = """
            {
                "id": ${createdBoard.id}
            }
        """.trimIndent()
        routineTestRead(baseURLBoard + createdBoard.id, resultJsonString)
    }

    @Test
    fun `005 - Get list of boards, with one board`() {
        val createdBoard = createBoard()
        val resultJsonString = mapper.writeValueAsString(createdBoard)

        routineTestIndex(baseURLBoard, resultJsonString)
    }

    @Test
    fun `011 - Exist column route`() {
        routineTestIndex(baseURLColumn)
    }

    @Test
    fun `012 - Post one column`() {
        val createdBoard = createBoard()
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
                "overflow": 0
            }
        """.trimIndent()

        routineTestCreate(baseURLColumn + "board/${createdBoard.id}", passedJsonString, resultJsonString)
    }

    //TODO: Искать сущьность перед созданием
    private fun createColumn(): Column {
        val createdBoard = createBoard()
        return getObjectRequest(
                baseURLColumn + "board/${createdBoard.id}",
                """
                        {
                            "name": "testColumn",
                            "order": 1
                        }
                    """,
                Column::class.java
        )
    }


    @Test
    fun `013 - Update column by id`() {
        val createdColumn = createColumn()
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
                "id": ${createdColumn.id}
            }
        """.trimIndent()

        routineTestUpdate(baseURLColumn + createdColumn.id, passedJsonString, resultJsonString)
    }

    @Test
    fun `014 - Find column by id`() {
        val createdColumn = createColumn()
        val resultJsonString = """
            {
                "id": ${createdColumn.id}
            }
        """.trimIndent()

        routineTestRead(baseURLColumn + createdColumn.id, resultJsonString)
    }

    @Test
    fun `015 - Get list of columns, with column`() {
        val createdColumn = createColumn()

        val resultJsonString = """
            [{
                "id": ${createdColumn.id}
            }]
        """.trimIndent()

        routineTestIndex(baseURLColumn, resultJsonString)
    }

    @Test
    fun `021 - Exist task route`() {
        routineTestIndex(baseURLTask)
    }

    @Test
    fun `022 - Post one task`() {
        val createdColumn = createColumn()
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
                "complexity": null
            }
        """.trimIndent()


        routineTestCreate(baseURLTask + "column/${createdColumn.id}", passedJsonString, resultJsonString)
    }

    //TODO: Искать сущьность перед созданием
    private fun createTask(): Task {
        val createdColumn = createColumn()
        return getObjectRequest(
                baseURLTask + "column/${createdColumn.id}",
                """
                        {
                            "name": "testTask",
                            "description": "testTaskDescription",
                            "priority" : 0
                        }
                    """,
                Task::class.java
        )
    }

    @Test
    fun `023 - Update first task`() {
        val createdTask = createTask()
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
                "id": ${createdTask.id}
            }
        """.trimIndent()

        routineTestUpdate(baseURLTask + createdTask.id, passedJsonString, resultJsonString)
    }

    @Test
    fun `024 - Find first task`() {
        val createdTask = createTask()
        val resultJsonString = """
            {
                "id": ${createdTask.id}
            }
        """.trimIndent()

        routineTestRead(baseURLTask + createdTask.id, resultJsonString)
    }

    @Test
    fun `025 - Get list of tasks, with one task`() {
        val createdTask = createTask()
        val resultJsonString = """
            [{
                "id": ${createdTask.id}
            }]
        """.trimIndent()

        routineTestIndex(baseURLTask, resultJsonString)
    }

    @Test
    fun `106 - Delete first board`() {
        val createdBoard = createBoard()
        routineTestDelete(baseURLBoard + createdBoard.id)
    }

    @Test
    fun `116 - Delete first column`() {
        val createdColumn = createColumn()
        routineTestDelete(baseURLColumn + createdColumn.id)
    }

    @Test
    fun `126 - Delete first task`() {
        val createdTask = createTask()
        routineTestDelete(baseURLTask + createdTask.id)
    }

	//TODO: Тесты маршрутов task/assign  и board/assign 
	//TODO: Тесты маршрутов task/promot	
}

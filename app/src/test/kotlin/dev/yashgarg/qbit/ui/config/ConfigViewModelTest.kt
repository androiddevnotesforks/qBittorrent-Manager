package dev.yashgarg.qbit.ui.config

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import dev.yashgarg.qbit.data.daos.ConfigDao
import dev.yashgarg.qbit.data.models.ConnectionType
import dev.yashgarg.qbit.data.models.ServerConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ConfigViewModelTest {
    private lateinit var viewModel: ConfigViewModel
    private val baseUrl: String by lazy { System.getenv("base_url") }
    private val config =
        ServerConfig(
            0,
            "TestServer",
            baseUrl,
            443,
            "admin",
            "windhoes",
            ConnectionType.HTTPS,
        )

    @Mock private lateinit var cfgDao: ConfigDao

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = ConfigViewModel(cfgDao)
    }

    @Test
    fun `check if form is valid by passing the details`() {
        runTest {
            viewModel.validateForm(
                config.serverName,
                config.baseUrl,
                config.port.toString(),
                config.connectionType.toString().lowercase(),
                config.username,
                config.password
            )

            assertTrue(
                viewModel.validationEvents.first() == ConfigViewModel.ValidationEvent.Success
            )
        }
    }

    @Test
    fun `check if client is connected and returns version`() {
        runTest {
            val response =
                viewModel.testConfig(
                    "${config.connectionType}://${config.baseUrl}",
                    config.username,
                    config.password,
                )

            when (response) {
                is Ok -> assertEquals(response.value, "v4.4.5")
                is Err -> throw response.error
            }
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}

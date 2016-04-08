package com.pik.base

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Ignore
import spock.lang.Shared


@WebAppConfiguration
@Ignore
class MvcIntegrationSpec extends IntegrationSpec {
    @Autowired
    protected WebApplicationContext webApplicationContext

    protected MockMvc mockMvc
    @Shared
    protected ObjectMapper mapper = new ObjectMapper()

    void setup() {
        ConfigurableMockMvcBuilder mockMvcBuilder = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        mockMvc = mockMvcBuilder.build()
    }

}

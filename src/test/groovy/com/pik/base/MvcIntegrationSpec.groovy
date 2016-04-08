package com.pik.base

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Ignore

/**
 * Source: https://github.com/jakubnabrdalik/hentai-cloudy-rental/blob/master/rentals/src/test/groovy/base/MvcIntegrationSpec.groovy
 */
@WebAppConfiguration
@Ignore
class MvcIntegrationSpec extends IntegrationSpec {

    @Autowired
    protected WebApplicationContext webApplicationContext

    protected MockMvc mockMvc

    void setup() {
        ConfigurableMockMvcBuilder mockMvcBuilder = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        mockMvc = mockMvcBuilder.build()
    }
}
package io.github.multicatch.cucumber.audit

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class ResourceLocatorTest {
    @Test
    fun `Should return file of an existing resource`() {
        val resourceFile = "test.txt".resourceFile()

        assertEquals(resourceFile?.name, "test.txt")
    }

    @Test
    fun `Should return null when resource does not exist`() {
        val resourceFile = "not_existent_resource.err".resourceFile()

        assertNull(resourceFile)
    }
}
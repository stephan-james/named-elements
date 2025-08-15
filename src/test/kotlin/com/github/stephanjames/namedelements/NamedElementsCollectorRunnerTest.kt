package com.github.stephanjames.namedelements

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class NamedElementsCollectorRunnerTest : BasePlatformTestCase() {

    private lateinit var runner: NamedElementsCollectorRunner

    override fun setUp() {
        super.setUp()
        runner = NamedElementsCollectorRunner()
    }

    fun testExecuteWithRealVirtualFileStructure() {
        val testFile = myFixture.addFileToProject("test.kt", """
            class TestClass {
                fun testMethod() {}
            }
        """.trimIndent())

        val testDir = testFile.containingDirectory

        val result = runner.execute(testDir)

        assertNotNull(result)
    }

    fun testExecuteWithMultipleFiles() {
        myFixture.addFileToProject("dir1/file1.kt", """
            class Class1 {
                val property1 = "test"
            }
        """.trimIndent())

        myFixture.addFileToProject("dir1/file2.java", """
            public class Class2 {
                public void method2() {}
            }
        """.trimIndent())

        myFixture.addFileToProject("dir1/subdir/file3.kt", """
            object Object3 {
                const val CONSTANT = 42
            }
        """.trimIndent())

        val rootDir = myFixture.findFileInTempDir("dir1")
        val psiDir = myFixture.psiManager.findDirectory(rootDir!!)

        val result = runner.execute(psiDir!!)

        assertNotNull(result)
        assertFalse(result.isEmpty())
    }

    fun testExecuteIgnoresBinaryFiles() {
        myFixture.addFileToProject("resource/Test.png", "")

        val srcDir = myFixture.findFileInTempDir("resource")
        val psiDir = myFixture.psiManager.findDirectory(srcDir)!!

        val result = runner.execute(psiDir)

        assertNotNull(result)
    }

    fun testShouldProcessFileWithRealFiles() {
        val kotlinFile = myFixture.addFileToProject("Test.kt", "class Test")
        val textFile = myFixture.addFileToProject("readme.txt", "some text")

        val method = runner.javaClass.getDeclaredMethod("shouldProcessFile", VirtualFile::class.java)
        method.isAccessible = true

        val kotlinResult = method.invoke(runner, kotlinFile.virtualFile) as Boolean
        val textResult = method.invoke(runner, textFile.virtualFile) as Boolean

        assertTrue(kotlinResult)
        assertTrue(textResult)
    }

    fun testShouldProcessFileWithDirectory() {
        myFixture.addFileToProject("subdir/test.kt", "class Test")
        val subdir = myFixture.findFileInTempDir("subdir")

        val method = runner.javaClass.getDeclaredMethod("shouldProcessFile", VirtualFile::class.java)
        method.isAccessible = true

        val result = method.invoke(runner, subdir) as Boolean

        assertFalse(result)
    }
}

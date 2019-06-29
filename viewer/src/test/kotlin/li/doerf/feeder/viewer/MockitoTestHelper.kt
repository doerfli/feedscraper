package li.doerf.feeder.viewer

import org.mockito.Mockito

class MockitoTestHelper {
    companion object {
        fun <T> any(): T {
            Mockito.any<T>()
            return uninitialized()
        }
        private fun <T> uninitialized(): T = null as T
    }
}


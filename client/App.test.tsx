import {cleanup, render, screen} from '@testing-library/react'
import App from './App'
import '@testing-library/jest-dom';

test('renders application', () => {
  // given
  cleanup()

  // when
  render(<App/>)

  // then
  const title = screen.getAllByText(/Acromere/i)
  expect(title[0]).toBeInTheDocument()
  expect(title[0]).toHaveTextContent('Acromere Weather')

  const now = new Date()
  const copyright = screen.getByText(/©/i)
  expect(copyright).toBeInTheDocument()
  expect(copyright).toHaveTextContent('© Acromere ' + now.getFullYear())
})


import {cleanup, render, screen} from '@testing-library/react'
import App from './App'
import '@testing-library/jest-dom';

test('renders application', () => {
  // given
  cleanup()

  // when
  render(<App/>)

  // then
  const copyright = screen.getByText(/Acromere/i)
  expect(copyright).toBeInTheDocument()
  expect(copyright).toHaveTextContent('Acromere Weather Website')
})


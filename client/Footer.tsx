export default function Footer() {

  const now = new Date()

  return (
    <div className="footer">
      <p>&copy; Acromere {now.getFullYear()}</p>
    </div>
  )

}
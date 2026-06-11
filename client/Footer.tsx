export default function Footer() {

  const now = new Date()

  return (
    <div className="footer">
      <p>Acromere Weather &copy; Acromere {now.getFullYear()}</p>
    </div>
  )

}